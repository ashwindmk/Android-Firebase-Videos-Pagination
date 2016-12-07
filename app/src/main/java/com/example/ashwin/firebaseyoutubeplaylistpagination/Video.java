package com.example.ashwin.firebaseyoutubeplaylistpagination;

/**
 * Created by ashwin on 2/12/16.
 */

public class Video
{
    private int position=0;
    private String id="", imageurl="", actionurl="", title="", type="";

    public Video()
    {
        //essential for firebase
    }

    public Video(int position, String type, String id, String title, String imageurl, String actionurl)
    {
        this.position = position;
        this.type = type;
        this.id = id;
        this.title = title;
        this.imageurl = imageurl;
        this.actionurl = actionurl;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public int getPosition()
    {
        return position;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setImageurl(String imageurl)
    {
        this.imageurl = imageurl;
    }

    public String getImageurl()
    {
        return imageurl;
    }

    public void setActionurl(String actionurl)
    {
        this.actionurl = actionurl;
    }

    public String getActionurl()
    {
        return actionurl;
    }

    @Override
    public String toString()
    {
        return "id : "+getId()+", title : "+getTitle();
    }
}
