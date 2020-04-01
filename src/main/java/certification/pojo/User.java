package certification.pojo;

public class User {
    private Integer userId;
    private String userName;
    private int userAge;

    public Integer getUserId(){
        return userId;
    }

    public void setUserId(Integer userId){
        this.userId = userId;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public int getUserAge(){
        return userAge;
    }

    public void setUserAge(Integer userAge){
        this.userAge = userAge;
    }

    @Override
    public String toString() {
        return "user [userId=" + userId + ", userName=" + userName
                + ", userAge=" + userAge + "]";
    }
}
