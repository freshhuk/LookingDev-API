package com.lookingdev.api.Services;

import com.lookingdev.api.Domain.Enums.QueueAction;
import com.lookingdev.api.Domain.Enums.QueueStatus;
import com.lookingdev.api.Domain.Models.DeveloperDTOModel;
import com.lookingdev.api.Domain.Models.MessageModel;
import com.lookingdev.api.Domain.Models.MessageStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class MessageService {

    @Value("${queueAPIStatus.name}")
    private String queueAPIStatus;


    private final RabbitTemplate rabbitTemplate;
    private List<DeveloperDTOModel> devModels;
    private String initStatusStackOverflow = "";
    private String initStatusGitHub = "";

    private CountDownLatch latch;


    @Autowired
    public MessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    /**
     * Method returns initialization status of StackOverflow microservice
     * @return initialization status
     */
    public String getInitStatusStackOverflow(){
        try {
            MessageStatus messageStatus = new MessageStatus(QueueAction.GET_INIT_STATUS_STACK_OVERFLOW, QueueAction.GET_INIT_STATUS_STACK_OVERFLOW.toString());

            latch = new CountDownLatch(1);

            sendStatusInQueue(queueAPIStatus, messageStatus);

            boolean received = latch.await(5, TimeUnit.SECONDS); // wait 5 seconds until we get the status

            if (!received) {
                System.out.println("Status not received in time");
                return null;
            }
            return !initStatusStackOverflow.isEmpty() ? initStatusStackOverflow : QueueStatus.BAD.toString();

        } catch (Exception ex) {
            System.out.println("Error getting init status " + ex);
            return QueueStatus.BAD.toString();
        }
    }

    /**
     * Method returns initialization status of GitHub microservice
     * @return initialization status
     */
    public String getInitStatusGitHub() {
        try {
            MessageStatus messageStatus = new MessageStatus(QueueAction.GET_INIT_STATUS_GIT, QueueAction.GET_INIT_STATUS_GIT.toString());

            latch = new CountDownLatch(1);

            sendStatusInQueue(queueAPIStatus, messageStatus);

            boolean received = latch.await(5, TimeUnit.SECONDS); // wait 5 seconds until we get the status

            if (!received) {
                System.out.println("Status not received in time");
                return null;
            }
            return !initStatusGitHub.isEmpty() ? initStatusGitHub : QueueStatus.BAD.toString();

        } catch (Exception ex) {
            System.out.println("Error getting init status " + ex);
            return QueueStatus.BAD.toString();
        }
    }


    /**
     * Method init GiHub database with users
     *
     * @return Status code
     */
    public String initGitDatabase() {
        try {
            MessageStatus messageStatus = new MessageStatus(QueueAction.INIT_DB_GIT, QueueAction.INIT_DB_GIT.toString());
            sendStatusInQueue(queueAPIStatus, messageStatus);
            return QueueStatus.DONE.toString();
        } catch (Exception ex) {
            System.out.println("Error with init db " + ex);
            return QueueStatus.BAD.toString();
        }
    }

    /**
     * Method init stack database with users
     *
     * @return Status code
     */
    public String initStackDatabase() {
        try {
            MessageStatus messageStatus = new MessageStatus(QueueAction.INIT_DB_STACK_OVERFLOW, QueueAction.INIT_DB_STACK_OVERFLOW.toString());
            sendStatusInQueue(queueAPIStatus, messageStatus);
            return QueueStatus.DONE.toString();
        } catch (Exception ex) {
            System.out.println("Error with init db " + ex);
            return QueueStatus.BAD.toString();
        }
    }
    /**
     * Get users from gitHub
     * @param page number page with users
     * @return list with users
     */
    public List<DeveloperDTOModel> getGitUsers(int page) {
        try {
            MessageStatus message = new MessageStatus(QueueAction.GET_GIT_DEV, page + "");

            latch = new CountDownLatch(1);  // set waiting one status

            sendStatusInQueue(queueAPIStatus, message);

            boolean received = latch.await(5, TimeUnit.SECONDS); // wait 5 seconds until we get the status

            if (!received) {
                System.out.println("Status not received in time");
                return null;
            }
            return !devModels.isEmpty() ? devModels : null;


        } catch (Exception ex) {
            System.out.println("Error with get gitHub users: " + ex);
            return null;
        }
    }

    /**
     * Get users from Stack Overflow
     * @param page number page with users
     * @return list with users
     */
    public List<DeveloperDTOModel> getStackOverflowUsers(int page) {
        try {
            MessageStatus message = new MessageStatus(QueueAction.GET_STACK_USER, page + "");

            latch = new CountDownLatch(1);  // set waiting one status

            sendStatusInQueue(queueAPIStatus, message);

            boolean received = latch.await(5, TimeUnit.SECONDS); // wait 5 seconds until we get the status

            if (!received) {
                System.out.println("Status not received in time");
                return null;
            }
            return !devModels.isEmpty() ? devModels : null;


        } catch (Exception ex) {
            System.out.println("Error with get gitHub users: " + ex);
            return null;
        }
    }

    /**
     * Get users from gitHub and Stack Overflow
     * @param numPage number pge with users
     * @return list with users
     */
    public List<DeveloperDTOModel> getAllUsers(int numPage) {
        try {
            MessageStatus message = new MessageStatus(QueueAction.GET_ALL, numPage + "");

            latch = new CountDownLatch(1);  // set waiting one status

            sendStatusInQueue(queueAPIStatus, message);

            boolean received = latch.await(5, TimeUnit.SECONDS); // wait 5 seconds until we get the status

            if (!received) {
                System.out.println("Status not received in time");
                return null;
            }
            return !devModels.isEmpty() ? devModels : null;


        } catch (Exception ex) {
            System.out.println("Error with get gitHub users: " + ex);
            return null;
        }
    }


    /* Listeners for queue */
    /* for GitHub */
    @RabbitListener(queues = "GitStatusQueue")
    public void getGitMessageInQueue(MessageModel models) {

        if ( models != null && models.getAction().equals(QueueAction.GET_GIT_DEV) && !models.getDeveloperProfiles().isEmpty()) {
            devModels = models.getDeveloperProfiles();
            latch.countDown();
            System.out.println("Developers was got: " + models.getDeveloperProfiles());
        }
    }
    /* for Stack Overflow */
    @RabbitListener(queues = "StackOverflowStatusQueue")
    public void getStackMessageInQueue(MessageModel models) {

        if (models != null && models.getAction().equals(QueueAction.GET_STACK_USER) && !models.getDeveloperProfiles().isEmpty()) {
            devModels = models.getDeveloperProfiles();
            latch.countDown();
            System.out.println("Developers was got: " + models.getDeveloperProfiles());
        }
    }
    /* for init status StackOverflow */
    @RabbitListener(queues = "StackOverflowInitStatusQueue")
    public void getInitStatusStackMessageInQueue(MessageStatus status) {

        if (status != null){
            initStatusStackOverflow = status.getStatus();
            latch.countDown();
            System.out.println("Init status StackOverflow was got");
        }
    }

    /* for init status GitHub */
    @RabbitListener(queues = "GitInitStatusQueue")
    public void getInitStatusGitMessageInQueue(MessageStatus status) {

        if (status != null){
            initStatusGitHub = status.getStatus();
            latch.countDown();
            System.out.println("Init status GitHub was got");
        }
    }

    /**
     * Method for sending status message in queue
     *
     * @param queueName queue name
     * @param message   message which be sent in the queue
     */
    private void sendStatusInQueue(String queueName, MessageStatus message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }


}
