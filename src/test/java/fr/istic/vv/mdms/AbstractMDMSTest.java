package fr.istic.vv.mdms;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * Created by ebousse on 22/10/14.
 */
public abstract class AbstractMDMSTest {


    // Parameters
    static String dockerURI = "http://127.0.0.1:5555";
    static String redisImageName = "redis";
    static String mdmsImageName = "maxleiko/mdms";
    static String redisContainerName = "tp-mdms-redis";
    static String mdmsContainerName = "tp-mdms";
    protected static int portForward = 8080;
    static DockerClient dockerClient;

    static String asString(InputStream response) {
        StringWriter logwriter = new StringWriter();
        try {
            LineIterator itr = IOUtils.lineIterator(
                    response, "UTF-8");
            while (itr.hasNext()) {
                String line = itr.next();
                logwriter.write(line + (itr.hasNext() ? "\n" : ""));
                //System.out.println("line: " + line);
            }
            return logwriter.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(response);
        }
    }


    private static boolean downloadImage(String name) {
        System.out.println("Downloading/checking image " + name + "...");
        PullImageCmd response = dockerClient.pullImageCmd(name);

        // Hopefully ok if written somewhere "download complete"
        return response.getTag().contains("Download complete");

    }


    private static void destroyContainers() {
        // Finding the list of all containers of the system
        List<Container> allContainers;
        allContainers = dockerClient.listContainersCmd().withShowAll(true).exec();
        // We remove all running containers
        for (Container c : allContainers) {
            if (c.getNames() == null) {
                break;
            }
            for (String s : c.getNames()) {
                if (s.equals("/" + redisContainerName) || s.equals("/" + mdmsContainerName)) {
                    System.out.println("Removing container " + c.getId());
                    dockerClient.killContainerCmd(c.getId()).exec();
                    dockerClient.removeContainerCmd(c.getId()).withForce().exec();
                }
            }
        }
    }

    private static void disableLogs() {
        java.util.logging.Logger.getGlobal().setLevel(Level.OFF);
        LogManager lm = java.util.logging.LogManager.getLogManager();

        Enumeration<String> loggerNames = lm.getLoggerNames();

        while (loggerNames.hasMoreElements()) {
            String next = loggerNames.nextElement();
            lm.getLogger(next).setLevel(Level.OFF);
        }

    }

    @Before
    public void prepareWebApp() {

        // We disable the logs of the Docekr API (uncomment to debug docker connection)
        disableLogs();

        // Connecting to the docker daemon
        System.out.println("Connecting to docker URI " + dockerURI + " ...");
        dockerClient = DockerClientBuilder.getInstance(dockerURI).build();
        destroyContainers();

        // We create and start the DB container
        System.out.println("Starting redis container...");
        CreateContainerResponse response;
        response = dockerClient.createContainerCmd(redisImageName).withName(redisContainerName).exec();
        dockerClient.startContainerCmd(response.getId()).exec();

        // We create a 8080:8080 binding for the web container
        ExposedPort tcpPort = ExposedPort.tcp(portForward);
        Ports portBindings = new Ports();
        portBindings.bind(tcpPort, Ports.Binding(portForward));

        // We create the web container
        System.out.println("Starting fr.istic.vv.mdms container...");
        CreateContainerCmd container = dockerClient.createContainerCmd(mdmsImageName);
        container.withName(mdmsContainerName);
        container.withTty(true);
        container.withExposedPorts(tcpPort);
        container.withLinks(new Link(redisContainerName, redisContainerName));
        container.withPortBindings(portBindings);
        response = container.exec();
        StartContainerCmd instance = dockerClient.startContainerCmd(response.getId());
        instance.exec();

        System.out.println("Success! Web application available here: http://localhost:" + portForward);

    }

    @After
    public void shutdownWebApp() {
        System.out.println("Testing over, destroying the webapp...");
        destroyContainers();
    }


}
