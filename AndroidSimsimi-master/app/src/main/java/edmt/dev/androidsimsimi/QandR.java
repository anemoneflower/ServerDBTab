package edmt.dev.androidsimsimi;

public class QandR {
    public String questionStr;
    public String responseStr;
    public boolean isSend;

    public QandR(){
        this.questionStr = questionStr;
        this.isSend = isSend;
        this.responseStr = responseStr;
    }
    public void setQuestion(String question){questionStr= question;}

    public void setResponse(String response){responseStr=response;}

    public String getQuestionStr() {return questionStr;}
    public String getResponseStr() {return responseStr;}




//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }


}
