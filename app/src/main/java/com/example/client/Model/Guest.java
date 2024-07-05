package com.example.client.Model;

public class Guest {
    private String _id="";
        private String name = "";
        private String phoneNumber;
        private int peopleCount;
        private String group;
        private String status;
        private String comments;
        private String event;
        private long createdAt;
        private long updatedAt;

         public Guest(String name,String phoneNumber,int peopleCount, String group,String status,String comments){
             this.name=name;
             this.phoneNumber=phoneNumber;
             this.peopleCount=peopleCount;
             this.group=group;
             this.status=status;
             this.comments=comments;
         }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


        @Override
        public String toString() {
            return "Guest{" +
                    "name='" + name + '\'' +
                    ", phone='" + phoneNumber + '\'' +
                    ", status=" + status +
                    "guests='" + peopleCount + '\'' +
                    ", group='" + group + '\'' +
                    ", comments=" + comments +
                    '}';
        }

    public String getComments() {
             return this.comments;
    }
}


