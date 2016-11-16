/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biocomp;

import java.util.Arrays;

/**
 *
 * @author Charlie
 */
public class DataFileFloat {

  float[] variable;
  int output;

  public DataFileFloat(float[] variable, int output) {
    this.variable = variable;
    this.output = output;
  }

  public float[] getVariable() {
    return variable;
  }

  public int getOutput() {
    return output;
  }

  public void setVariable(float[] variable) {
    this.variable = variable;
  }

  public void setOutput(int output) {
    this.output = output;
  }

  @Override
  public String toString() {
    return "DataFile{" + "variable=" + Arrays.toString(variable) + ", output=" + output + '}';
  }

}
