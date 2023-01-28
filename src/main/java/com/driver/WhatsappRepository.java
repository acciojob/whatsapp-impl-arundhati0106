package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.

    private HashMap<String, String> newUser;           //save name, phone of user
    private HashMap<Integer, String> messageContent;   //send message to which number

    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.newUser = new HashMap<>();
        this.messageContent = new HashMap<>();

        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = -1;
    }

    public String createUser(String name, String mobile) throws Exception {
        //If the mobile number exists in database, throw "User already exists" exception
        //Otherwise, create the user and return "SUCCESS"

        if (userMobile.contains(mobile)) {
            throw new Exception("User already exists");
        }

        userMobile.add(mobile);
        newUser.put(name, mobile);

        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        // The list contains at least 2 users where the first user is the admin. A group has exactly one admin.
        // If there are only 2 users, the group is a personal chat and the group name should be kept as the name of the second user(other than admin)
        // If there are 2+ users, the name of group should be "Group count". For example, the name of first group would be "Group 1", second would be "Group 2" and so on.
        // Note that a personal chat is not considered a group and the count is not updated for personal chats.
        // If group is successfully created, return group.

        //For example: Consider userList1 = {Alex, Bob, Charlie}, userList2 = {Dan, Evan}, userList3 = {Felix, Graham, Hugh}.
        //If createGroup is called for these userLists in the same order, their group names would be "Group 1", "Evan", and "Group 2" respectively.

        if(users.size() > 2) {
            customGroupCount++;
            Group group = new Group("Group "+customGroupCount, users.size());
            groupUserMap.put(group, users);
            adminMap.put(group, users.get(0));
            groupMessageMap.put(group, new ArrayList<Message>());
            return group;
        }

        Group group = new Group(users.get(1).getName(), users.size());
        groupUserMap.put(group, users);
        adminMap.put(group, users.get(0));
        groupMessageMap.put(group, new ArrayList<Message>());
        return group;
    }

    public int createMessage(String content){
        // The 'i^th' created message has message id 'i'.
        // Return the message id.

        messageId++;
        Message message = new Message(messageId, content);
        messageContent.put(messageId, content);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "You are not allowed to send message" if the sender is not a member of the group
        //If the message is sent successfully, return the final number of messages in that group.

        //non existent group
        if(!groupUserMap.containsKey(group.getName())){
            throw new Exception("Group does not exist");
        }

        //non existent group member
        boolean checkUserInGroup = false;
        for(User user : groupUserMap.get(group)){
            if(sender.getMobile().equals(user.getMobile())){
                checkUserInGroup = true;
                break;
            }
        }
        if(!checkUserInGroup){
            throw new Exception("You are not allowed to send message");
        }

        //send message in group
        senderMap.put(message, sender);
        List<Message> list = groupMessageMap.get(group);
        list.add(message);
        groupMessageMap.put(group, list);
        return list.size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        //Throw "User is not a participant" if the user is not a part of the group
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.

        //group doesn't exist
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }

        //approver isn't the admin
        if(adminMap.get(group)!=approver){
            throw new Exception("Approver does not have rights");
        }

        //user isn't part of group
        boolean checkUserInGroup = false;
        for(User i : groupUserMap.get(group)){
            if(user.getMobile().equals(i.getMobile())){
                checkUserInGroup = true;
                break;
            }
        }
        if(!checkUserInGroup){
            throw new Exception("User is not a participant");
        }

        //change admin
        adminMap.put(group, user);
        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception {
        //This is a bonus problem and does not contains any marks
        //A user belongs to exactly one group
        //If user is not found in any group, throw "User not found" exception
        //If user is found in a group and it is the admin, throw "Cannot remove admin" exception
        //If user is not the admin, remove the user from the group, remove all its messages from all the databases, and update relevant attributes accordingly.
        //If user is removed successfully, return (the updated number of users in the group + the updated number of messages in group + the updated number of overall messages)

        Group group1 = new Group();
        User user1 = new User();

        //user not in group
        boolean checkUserInGroup = false;
        for(Group group : groupUserMap.keySet()){
            for(User u : groupUserMap.get(group)){
                if(u.getMobile().equals(user.getMobile())){
                    user1 = u;
                    group1 = group;
                    checkUserInGroup = true;
                    break;
                }
            }
        }
        if(!checkUserInGroup){
            throw new Exception("User not found");
        }

        //user is the admin
        if(adminMap.get(group1)==user){
            throw new Exception("Cannot remove admin");
        }

        //remove user
        userMobile.remove(user1.getMobile());
        List<User> list = groupUserMap.get(group1);
        list.remove(user1);
        groupUserMap.put(group1,list);

        List<Message> listOfMessage = groupMessageMap.get(group1);
        for(Message message : listOfMessage){
            if(senderMap.get(message)==user1){
                senderMap.remove(message);
            }
            listOfMessage.remove(message);
        }

        return groupUserMap.get(group1).size() + groupMessageMap.get(group1).size() + senderMap.size();
    }

    //didnt solve
    public String findMessage(Date start, Date end, int K) throws Exception{
        //This is a bonus problem and does not contains any marks
        // Find the Kth latest message between start and end (excluding start and end)
        // If the number of messages between given time is less than K, throw "K is greater than the number of messages" exception

        return "didn't solve yet";
    }
}
