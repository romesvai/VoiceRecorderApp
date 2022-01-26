package com.romes.voicerecorderapp

import java.util.*
import java.util.concurrent.TimeUnit

public class TimeAgo {
    fun  getTimeAgo(duration : Long ) : String {
        val now : Date = Date()
         val seconds : Long = TimeUnit.MILLISECONDS.toSeconds(now.time-duration)
        val minutes : Long = TimeUnit.MILLISECONDS.toMinutes(now.time-duration)
        val hours : Long = TimeUnit.MILLISECONDS.toHours(now.time-duration)
        val days : Long = TimeUnit.MILLISECONDS.toDays(now.time-duration)

        if(seconds<60){
            return "just now"
        }
        else if(minutes==1L){
            return " a minute ago"
        }
        else if(minutes in 2..59){
            return  minutes.toString() + "minutes ago"
        }

        else if(hours == 1L){
            return "an hour ago"
        }
        else if(hours in 2..59){
            return  hours.toString() + "hours ago"
        }
        else if(days == 1L){
            return "an day ago"
        }
        else{
            return  days.toString() + "days ago"
        }




    }
}