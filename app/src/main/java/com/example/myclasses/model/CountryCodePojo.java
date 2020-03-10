package com.example.myclasses.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryCodePojo{

   @SerializedName("response_code")
   private int responseCode;

   @SerializedName("type")
   private String type;

   @SerializedName("message")
   private String message;

   @SerializedName("status")
   private boolean status;

   @SerializedName("data")
   private List<CountryCodePojoItem> countryCodePojo;

   public void setResponseCode(int responseCode){
       this.responseCode = responseCode;
   }

   public int getResponseCode(){
       return responseCode;
   }

   public void setType(String type){
       this.type = type;
   }

   public String getType(){
       return type;
   }

   public void setMessage(String message){
       this.message = message;
   }

   public String getMessage(){
       return message;
   }

   public void setStatus(boolean status){
       this.status = status;
   }

   public boolean isStatus(){
       return status;
   }

   public void setCountryCodePojo(List<CountryCodePojoItem> countryCodePojo){
       this.countryCodePojo = countryCodePojo;
   }

   public List<CountryCodePojoItem> getCountryCodePojo(){
       return countryCodePojo;
   }

   @Override
    public String toString(){
       return
           "CountryCodePojo{" +
           "response_code = '" + responseCode + '\'' +
           ",type = '" + type + '\'' +
           ",message = '" + message + '\'' +
           ",status = '" + status + '\'' +
           ",countryCodePojo = '" + countryCodePojo + '\'' +
           "}";
       }
}