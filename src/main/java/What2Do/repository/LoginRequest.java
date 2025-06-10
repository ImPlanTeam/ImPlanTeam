package What2Do.repository;


import lombok.Data;

@Data
public class LoginRequest {

        private String id;
        private String pass;

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getPass() {
                return pass;
        }

        public void setPass(String pass) {
                this.pass = pass;
        }
}
