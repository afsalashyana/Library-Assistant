package library.assistant.ui.listmember;

public class MemberListController {
    public static class Member {
        private String id;
        private String name;
        private String mobile;
        private String email;

        public Member(String name, String id, String mobile, String email) {
            this.name = name;
            this.id = id;
            this.mobile = mobile;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getMobile() {
            return mobile;
        }

        public String getEmail() {
            return email;
        }
    }
}
