package com.horizonx.simplesaving;

import android.content.Context;

public class CoreAPIs {
     private DataCenter dc;
     private Context context;
     public CoreAPIs(){
    	 dc=DataCenter.getInstance();
     }
     public int calculateExpection(){
    	 if(DataCenter.todayExpense!=-1)
    	  return (DataCenter.dailyEarning*DataCenter.totalDaysInAMonth-DataCenter.expenseBefore-DataCenter.fixedOutcome-DataCenter.monthlyGoal-DataCenter.todayExpense)/DataCenter.daysLeft;
    	 else
    	  return (DataCenter.dailyEarning*DataCenter.totalDaysInAMonth-DataCenter.expenseBefore-DataCenter.fixedOutcome-DataCenter.monthlyGoal)/DataCenter.daysLeft; 
     }
}
