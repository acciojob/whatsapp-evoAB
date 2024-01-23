package com.driver;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class WhatsappService {
    private WhatsappRepository whatsappRepository;
    public String createUser(String name, String mobile) throws Exception{
        if(whatsappRepository.getUserMobile().contains(name))
            throw new Exception("User already exists");

        User user = new User(name, mobile);

        whatsappRepository.getUserMobile().add(user);
        return "SUCCESS";
    }
    public Group createGroup(List<User> users){
        Group newGroup=null;
        if(users.size()==2){
            String groupName = users.get(1).getName();
            newGroup = new Group(groupName, 2);
        }else {
            String groupName = "Group "+ whatsappRepository.getCustomGroupCount();
            int noOfParicipants = users.size();
            User admin = users.get(0);
            newGroup = new Group(groupName, noOfParicipants);
            whatsappRepository.getGroupUserMap().put(newGroup, users);
            whatsappRepository.getAdminMap().put(newGroup, admin);
            whatsappRepository.setCustomGroupCount(whatsappRepository.getCustomGroupCount()+1);
        }
        return newGroup;
    }

    public int createMessage(String content){
        int messageId = whatsappRepository.getMessageId();
        Message message = new Message(messageId, content, new Date());
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "You are not allowed to send message" if the sender is not a member of the group
        //If the message is sent successfully, return the final number of messages in that group.
        if(whatsappRepository.getGroupUserMap().containsKey(group))
            throw new Exception("Group does not exist");

        List<User> users = whatsappRepository.getGroupUserMap().get(group);

        if(!users.contains(sender))
            throw new Exception("You are not allowed to send message");

        List<Message> messages = whatsappRepository.getGroupMessageMap().get(group);
        messages.add(message);
        whatsappRepository.getGroupMessageMap().put(group, messages);
        return message.getId();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        //Throw "User is not a participant" if the user is not a part of the group
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.

        if (!whatsappRepository.getGroupMessageMap().containsKey(group))
            throw new Exception("Group does not exist");

        if (!whatsappRepository.getAdminMap().containsKey(approver))
            throw new Exception("Approver does not have rights");

        whatsappRepository.getAdminMap().remove(approver);
        whatsappRepository.getAdminMap().put(group, user);
        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception{
        //This is a bonus problem and does not contains any marks
        //A user belongs to exactly one group
        //If user is not found in any group, throw "User not found" exception
        //If user is found in a group and it is the admin, throw "Cannot remove admin" exception
        //If user is not the admin, remove the user from the group, remove all its messages from all the databases, and update relevant attributes accordingly.
        //If user is removed successfully, return (the updated number of users in the group + the updated number of messages in group + the updated number of overall messages)

        return 0;
    }

    public String findMessage(Date start, Date end, int K) throws Exception{
        //This is a bonus problem and does not contains any marks
        // Find the Kth latest message between start and end (excluding start and end)
        // If the number of messages between given time is less than K, throw "K is greater than the number of messages" exception

        return null;
    }
}
