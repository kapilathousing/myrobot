package com.kapil.robot.util;

import android.content.Context;

import com.kapil.robot.model.Feed;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kapilboss on 06/05/15.
 */
public class XmlParsing {

   public ArrayList<Feed> getFeedList(String response, Context context, int website){

        ArrayList<Feed> feeds=new ArrayList<Feed>();
        try {
            Date lastFeedStamp= Misc.getFeedSyncTime(context,website);
            MyLog.debugLog("Last Feed time:" + lastFeedStamp.toString());
            StringReader xmlResponse=new StringReader(response);
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser parser=factory.newPullParser();
            parser.setInput(xmlResponse);
            int eventType=parser.getEventType();
            boolean stopParsing=false;
            Feed newFeed=null;
            boolean feedTimeUpdated=false;

            String updated=null;
            while(eventType!=XmlPullParser.END_DOCUMENT && stopParsing!=true){
                String tag=parser.getName();

                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if(tag.equals("entry")||tag.equals("item")){
                            newFeed=new Feed();
                            newFeed.setWebsite(website);

                        }else if(tag.equals("link")&& newFeed!=null){
                            if(website!=4) {
                                int attribute_len = parser.getAttributeCount() - 1;
                                while (attribute_len > 0) {
                                    if (parser.getAttributeName(attribute_len).equals("href")) {
                                        newFeed.link = parser.getAttributeValue(attribute_len);
                                        break;
                                    }
                                    attribute_len--;
                                }
                            }else{
                                newFeed.link =parser.nextText();
                            }

                            //newFeed.link=parser.nextText();
                            MyLog.debugLog("id:" + newFeed.link);

                    }else if(tag.equals("title")&& newFeed!=null){
                            newFeed.title=parser.nextText();
                            newFeed.title=newFeed.title.replace("Answer by CommonsWare for ", "");
                            newFeed.title=newFeed.title.replace("Comment by CommonsWare on ","");
                     }else if((tag.equals("pubDate")||tag.equals("updated"))&& newFeed!=null){
                            if(website!=4)
                            updated=parser.nextText().substring(0,19)+"Z";
                            else
                                updated=parser.nextText();



                            MyLog.debugLog("Title& update time: "+newFeed.title);
                            if(feedTimeUpdated==false)
                            {
                                Misc.updateFeedSyncTime(context,updated,website);
                                feedTimeUpdated=true;
                            }

                            Date currentFeedStamp=Misc.convertStringToDate(updated,website);
                            MyLog.debugLog("date1:"+lastFeedStamp+" date2:"+currentFeedStamp);


                            long diff=lastFeedStamp.compareTo(currentFeedStamp);
                                   //
                            MyLog.debugLog("Updated on:"+updated+" diff:"+diff);

                            if(diff>=0)
                                stopParsing=true;

                     }

                        break;
                    case XmlPullParser.END_TAG:
                        if((tag.equals("entry")||tag.equals("item"))&& stopParsing!=true){
                            newFeed.title=newFeed.title+" | "+ updated;
                           feeds.add(newFeed);

                        }
                        break;

                }
                parser.next();
                eventType=parser.getEventType();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  feeds;
    }
}
