import java.util.ArrayList;
import java.util.List;


class VirtualAssistant {
     private String assistantName;
     private Double version;

     public VirtualAssistant(String assistantName, Double version) {
         this.assistantName = assistantName;
         this.version = version;
     }

     public String getAssistantName() {
         return assistantName;
     }

     public void setAssistantName(String assistantName) {
         this.assistantName = assistantName;
     }

     public Double getVersion() {
         return version;
     }

     public void setVersion(Double version) {
         this.version = version;
     }

     public String greetUser() {
         return "Hello! I am your Virtual Assistant.";
     }

     public String performTask(String task) {
         return "Sorry, I can't do that.";
     }
 }

     class HomeAssistant extends VirtualAssistant {
         private boolean isLightOn;

         public HomeAssistant(String assistantName, Double version) {
             super(assistantName, version);
             this.isLightOn = false;
         }

         public boolean isLightOn() {
             return isLightOn;
         }

         public void setLightOn(boolean isLightOn) {
             this.isLightOn = isLightOn;
         }

         @Override
         public String greetUser() {
             return "Hello! I’m your Home Assistant. How can I help to control your home today?";
         }
         @Override
         public String performTask(String task) {
             if (task.equalsIgnoreCase("turn on lights")) {
                 if (isLightOn) {
                     return "The lights are already turned on.";
                 }
                 isLightOn = true;
                 return "Turning on the lights!";
             } else if (task.equalsIgnoreCase("turn off lights")) {
                 if (!isLightOn) {
                     return "The lights are already turned off.";
                 }
                 isLightOn = false;
                 return "Turning off the lights!";
             }
             return "Sorry, I can't do that.";
         }
     }

     class PersonalFinanceAssistant extends VirtualAssistant {
         private double currentBalance;

         public PersonalFinanceAssistant(String assistantName, Double version) {
             super(assistantName, version);
             this.currentBalance = 500.0;
         }

         public double getCurrentBalance() {
             return currentBalance;
         }

         public void setCurrentBalance(double currentBalance) {
             this.currentBalance = currentBalance;
         }

         @Override
         public String greetUser() {
             return "Hi! I’m your Finance Assistant. Let’s manage your money wisely!";
         }

         @Override
         public String performTask(String task) {
             if (task.equalsIgnoreCase("show balance")) {
                 return "Your current balance: " + currentBalance + " dollars";
             } else if (task.startsWith("deposit money")) {
                 try {
                     double amount = Double.parseDouble(task.split(" ")[2]);
                     currentBalance += amount;
                     return amount + " dollars is deposited into your account. Your current balance: " + currentBalance + " dollars";
                 } catch (Exception e) {
                     return "Invalid deposit command.";
                 }
             } else if (task.startsWith("withdraw")) {
                 try {
                     double amount = Double.parseDouble(task.split(" ")[1]);
                     if (currentBalance < amount) {
                         return "Sorry, insufficient balance!";
                     }
                     currentBalance -= amount;
                     return amount + " dollars is withdrawn from your account. Your current balance: " + currentBalance + " dollars";
                 } catch (Exception e) {
                     return "Invalid withdraw command.";
                 }
             }
             return "I don’t know how to do that.";
         }
     }

     class LanguageTranslatorAssistant extends VirtualAssistant {
         private String lastTranslatedWord;

         public LanguageTranslatorAssistant(String assistantName, Double version) {
             super(assistantName, version);
             this.lastTranslatedWord = "None";
         }

         public String getLastTranslatedWord() {
             return lastTranslatedWord;
         }

         public void setLastTranslatedWord(String lastTranslatedWord) {
             this.lastTranslatedWord = lastTranslatedWord;
         }

         @Override
         public String greetUser() {
             return "Bonjour! Hola! Hello! I’m your Language Translator AI!";
         }
         @Override
         public String performTask(String task) {
             if (task.equalsIgnoreCase("translate hello to Spanish")) {
                 lastTranslatedWord = "Hola";
                 return "Hello in Spanish is Hola.";
             } else if (task.equalsIgnoreCase("translate thank you to French")) {
                 lastTranslatedWord = "Merci";
                 return "Thank you in French is Merci.";
             }
             return "I don’t know that language yet.";
         }

}
  class AssistantManager {
     private List<VirtualAssistant> assistants;

     public AssistantManager() {
         this.assistants = new ArrayList<>();
     }

     public List<VirtualAssistant> getAssistants() {
         return assistants;
     }

     public void setAssistants(List<VirtualAssistant> assistants) {
         this.assistants = assistants;
     }

     public void addAssistant(VirtualAssistant assistant) {
         assistants.add(assistant);
     }

     public void removeAssistant(VirtualAssistant assistant) {
         assistants.remove(assistant);
     }
      public List<String> interactWithAll(String task) {
          List<String> responses = new ArrayList<>();
          for (VirtualAssistant assistant : assistants) {
              String greeting = assistant.greetUser();
              String response = assistant.performTask(task);
              System.out.println(greeting);
              System.out.println(response);
              responses.add(greeting);
              responses.add(response);
          }
          return responses;
      }
 }
    class Main{
     public static void main(String[] args) {
        VirtualAssistant va = new VirtualAssistant("VA", 1.0);
        HomeAssistant ha = new HomeAssistant("SmartHome", 2.0);
        PersonalFinanceAssistant pf = new PersonalFinanceAssistant("SmartPersonal", 3.0);
        LanguageTranslatorAssistant lt = new LanguageTranslatorAssistant("SmartLanguage", 4.0);
        System.out.println(va.greetUser());
        System.out.println(ha.greetUser());
        System.out.println(pf.greetUser());
        System.out.println(lt.greetUser());
        System.out.println(ha.performTask("turn on lights"));
        System.out.println(pf.performTask("show balance"));
        System.out.println(pf.performTask("deposit 100"));
        System.out.println(pf.performTask("withdraw 70"));
        System.out.println(lt.greetUser());
        System.out.println(lt.performTask("translate hello to Spanish"));
        System.out.println(lt.performTask("translate thank you to French"));
        AssistantManager manager = new AssistantManager();
        manager.addAssistant(ha);
        manager.addAssistant(pf);
        manager.addAssistant(lt);

        manager.interactWithAll("turn on lights");
        manager.interactWithAll("translate hello to Spanish");
        manager.interactWithAll("translate thank you to French");
        manager.interactWithAll("show balance");
        manager.interactWithAll("deposit 100");
        manager.interactWithAll("withdraw 70");


     }
}


