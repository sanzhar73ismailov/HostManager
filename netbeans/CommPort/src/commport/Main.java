/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package commport;

import java.util.List;

/**
 *
 * @author sanzhar.ismailov
 */
public class Main {
    List <Object> optionList;
    private String name;
    private String size;

    public void setName(String name) {
        this.name = name;
    }

    public void setOptionList(List<Object> optionList) {
        this.optionList = optionList;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Main{" + "name=" + name + "size=" + size + '}';
    }





    public static void main(String[] args) {
//         java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new NewJFrame().setVisible(true);
//            }
//        });
    }

}
