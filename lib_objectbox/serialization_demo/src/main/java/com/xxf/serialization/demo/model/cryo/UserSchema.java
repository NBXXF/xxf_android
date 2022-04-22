package com.xxf.serialization.demo.model.cryo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;
import io.protostuff.runtime.RuntimeSchema;

public class UserSchema implements Schema<User>
{
    public boolean isInitialized(User user)
    {
        return user.getEmail() != null;
    }

    public void mergeFrom(Input input, User user) throws IOException
    {
        while (true)
        {
            int number = input.readFieldNumber(this);
            switch(number)
            {
                case 0:
                    return;
                case 1:
                    user.setEmail(input.readString());
                    break;
                case 2:
                    user.setFirstName(input.readString());
                    break;
                case 3:
                    user.setLastName(input.readString());
                    break;
                case 4:
                    if (user.subNode == null)
                        user.subNode = new ArrayList<String>();
                    user.subNode.add(input.mergeObject(null, RuntimeSchema.getSchema(String.class)));
                    break;
                default:
                    input.handleUnknownField(number, this);
            }
        }
    }

    public void writeTo(Output output, User user) throws IOException
    {
//        if (user.getEmail() == null)
//            throw new UninitializedMessageException(user, this);

        output.writeString(1, user.getEmail(), false);

        if (user.getFirstName() != null)
            output.writeString(2, user.getFirstName(), false);

        if (user.getLastName() != null)
            output.writeString(3, user.getLastName(), false);

        if (user.subNode != null)
        {
            for (String friend : user.subNode)
            {
                if (friend != null)
                   // output.writeString(4,friend,true);
                    output.writeObject(4, friend, RuntimeSchema.getSchema(String.class), true);
            }
        }
    }

    public User newMessage()
    {
        return new User();
    }

    public Class<User> typeClass()
    {
        return User.class;
    }

    public String messageName()
    {
        return User.class.getSimpleName();
    }

    public String messageFullName()
    {
        return User.class.getName();
    }

    // the mapping between the field names to the field numbers.

    public String getFieldName(int number)
    {
        switch(number)
        {
            case 1:
                return "email";
            case 2:
                return "firstName";
            case 3:
                return "lastName";
            case 4:
                return "friends";
            default:
                return null;
        }
    }

    public int getFieldNumber(String name)
    {
        Integer number = fieldMap.get(name);
        return number == null ? 0 : number.intValue();
    }

    private static final HashMap<String,Integer> fieldMap = new HashMap<String,Integer>();
    static
    {
        fieldMap.put("email", 1);
        fieldMap.put("firstName", 2);
        fieldMap.put("lastName", 3);
        fieldMap.put("friends", 4);
    }
}