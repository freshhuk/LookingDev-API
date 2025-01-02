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

    private CountDownLatch latch;


    @Autowired
    public MessageService(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Method init database with users
     * @return Status code
     */
    public String initDataBase(){
        try {
            MessageStatus messageStatus = new MessageStatus(QueueAction.INIT_DB, QueueAction.INIT_DB.toString());
            sendStatusInQueue(queueAPIStatus,messageStatus);
            return QueueStatus.DONE.toString();
        } catch (Exception ex){
            System.out.println("Error with init db " + ex);
            return QueueStatus.BAD.toString();
        }
    }

    public List<DeveloperDTOModel> getGitUsers(){
        try{
            MessageStatus message = new MessageStatus(QueueAction.GET_GIT_DEV, QueueAction.GET_GIT_DEV.toString());

            latch = new CountDownLatch(1);  // set waiting one status

            sendStatusInQueue(queueAPIStatus, message);

            boolean received = latch.await(5, TimeUnit.SECONDS); // wait 5 seconds until we get the status

            if (!received) {
                System.out.println("Status not received in time");
                return null;
            }
            return !devModels.isEmpty() ? devModels : null;


        } catch (Exception ex){
            System.out.println("Error with get gitHub users: " + ex);
            return null;
        }
    }

    /* Listeners for queue */
    @RabbitListener(queues = "GitStatusQueue")
    public void getGitMessageInQueue(MessageModel models){
        if(models != null && !models.getDeveloperProfiles().isEmpty()){
            devModels = models.getDeveloperProfiles();
            latch.countDown();
            System.out.println("Developers was got: " + models.getDeveloperProfiles());
        }
    }

    /**
     * Method for sending status message in queue
     * @param queueName queue name
     * @param message message which be sent in the queue
     */
    private void sendStatusInQueue(String queueName, MessageStatus message){
        rabbitTemplate.convertAndSend(queueName, message);
    }
}
