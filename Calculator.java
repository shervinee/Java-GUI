/**
 * The first assignment simulator for calculator
 * 
 */
package assignment1;

/**
 * @author Shervin Shahidizandi
 * Created on /6/5/2017
 *
 */
public class Calculator {
    public double add(double operand1, double operand2){
	return operand1+operand2;
    }
    public double subtract(double operand1, double operand2){
	return operand1 - operand2;
    }
    public double multiply(double operand1, double operand2){
	return operand1 * operand2;
    }
    /**
     * for division 
     * might be updated to work with float numbers as well
     * @param operand1
     * @param operand2
     * @return
     */
    public double divide(double operand1, double operand2){
	return operand1/operand2;
    }
    public double mod(double operand1, double operand2){
	return operand1%operand2;
    }

}
