package com.example.hadad.towme.Others;

import com.example.hadad.towme.Tables.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadad on 1/29/2017.
 */

public class CommentsTable {
    public static final Map<Long, ArrayList<Comment>> COMMENTS_MAP = new HashMap<Long, ArrayList<Comment>>();

    public static ArrayList<Comment> getComments(Long id){
        if(COMMENTS_MAP.get(new Long(id)) == null)
            COMMENTS_MAP.put(new Long(id),new ArrayList<Comment>());
        return COMMENTS_MAP.get(id);
    }

    public static void addComment(Comment comm){
        if(COMMENTS_MAP.get(comm.getTowId()) == null)
            COMMENTS_MAP.put(comm.getTowId(),new ArrayList<Comment>());
        COMMENTS_MAP.get(comm.getTowId()).add(comm);

    }

    public static void hardCopy(Long tid,ArrayList<Comment> comments){
        COMMENTS_MAP.get(tid).clear();
        COMMENTS_MAP.get(tid).addAll(comments);
    }
}
