package com.softserve.edu11;
import java.util.Objects;

public class ValidLoginUser {
        private String email;
        private String password;
        private String name;


        public ValidLoginUser(String email, String password, String name) {
            this.email = email;
            this.password = password;
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }


        public String getUsername() {
            return name;
        }



        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ValidLoginUser that = (ValidLoginUser) o;
            return Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(email, password, name);
        }

        @Override
        public String toString() {
            return "TesterUser{" +
                    "email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    ", username='" + name + '\'' +
                    '}';
        }
    }

