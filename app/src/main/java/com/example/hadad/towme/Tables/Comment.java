package com.example.hadad.towme.Tables;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.example.hadad.towme.Others.Constants;

import java.util.UUID;

/**
 * Created by Omer on 16-Jan-17.
 */

@DynamoDBTable(tableName = Constants.Comment_TABLE)
public class Comment {

    private String id;
    private Long AuthorId;
    private Long TowId;
    private String Comment;

    public Comment(){}
    public Comment(Long TowId,Long AuthorId, String Comment){
        id = UUID.randomUUID().toString();
        this.TowId = TowId;
        this.AuthorId = AuthorId;
        this.Comment = Comment;
    }

    @DynamoDBHashKey(attributeName = "Id") //primary key
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @DynamoDBAttribute(attributeName ="AuthorId")
    public Long getAuthorId() { return AuthorId; }
    public void setAuthorId(Long authorId) { AuthorId = authorId; }

    @DynamoDBAttribute(attributeName ="TowId")
    public Long getTowId() {
        return TowId;
    }
    public void setTowId(Long towId) {
        TowId = towId;
    }


    @DynamoDBAttribute(attributeName ="Comment")
    public String getComment() { return Comment; }
    public void setComment(String comment) { Comment = comment; }


}
