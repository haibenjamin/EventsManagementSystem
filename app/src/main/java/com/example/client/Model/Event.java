package com.example.client.Model;

public class Event {


        private String name = "";
        private String dietry = "";
        private String phone = "";
        private boolean isComing = false;


        public Event() {}

        public String getName() {
            return name;
        }

        public Event setName(String name) {
            this.name = name;
            return this;
        }
    public Event setPhone(String number) {
        this.phone = number;
        return this;
    }

        public String getPhone() {
            return phone;
        }


        public String getDietry() {
            return dietry;
        }

        public Event setDietry(String dietry) {
            this.dietry = dietry;
            return this;
        }




        public boolean isComing() {
            return isComing;
        }

        public Event setComing(boolean coming) {
            isComing = coming;
            return this;
        }


        @Override
        public String toString() {
            return "Guest{" +
                    "name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
                    ", dietry=" + dietry +
                    ", isComing=" + isComing +
                    '}';
        }
    }


