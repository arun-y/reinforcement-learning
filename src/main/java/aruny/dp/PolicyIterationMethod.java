package aruny.dp;

import java.util.Arrays;

public class PolicyIterationMethod {

  private static final double GAMMA = 1;
  
  private static final double THETA = 10E-4;
  
  public enum Action {
    UP,
    DOWN,
    LEFT,
    RIGHT
  }
  
  private static final int[] states = 
      new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
  
  private int getStateRow(int state) {
    return state / 4;
  }
  
  private int getStateCol(int state) {
    return state % 4;
  }
  
  public int p(int nextState, int currentState, Action action) {
    int currentRow = getStateRow(currentState);
    int currentCol = getStateCol(currentState);
    int nextRow = getStateRow(nextState);
    int nextCol = getStateCol(nextState);
    
    //if terminating state, the probability of going to other state is zero
    if (currentState == 0 || currentState == 15) {
      if (currentState == nextState) {
        return 1;
      } else {
        return 0;
      }
    }
    
    switch (action) {
      case UP:
        if (currentCol == nextCol) {
          if (currentRow == 0 && nextRow == 0) {
             return 1;
          }
          if (currentRow == nextRow + 1) {
            return 1;
          }
          return 0;
        } else {
          return 0;
        }
      case DOWN:
        if (currentCol == nextCol) {
          if (currentRow == 3 && nextRow == 3) {
             return 1;
          }
          if (currentRow == nextRow - 1) {
            return 1;
          }
          return 0;
        } else {
          return 0;
        }
      case LEFT:
        if (currentRow == nextRow) {
          if (currentCol == 0 && nextCol == 0) {
             return 1;
          }
          if (currentCol == nextCol + 1) {
            return 1;
          }
          return 0;
        } else {
          return 0;
        }
      case RIGHT:
        if (currentRow == nextRow) {
          if (currentCol == 3 && nextCol == 3) {
             return 1;
          }
          if (currentCol == nextCol - 1) {
            return 1;
          }
          return 0;
        } else {
          return 0;
        }
    }
    return 0;
  }

  //initialize random policy
  private double pi[][] = new double[][] {
    //UP, DN, LT, RT 
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d},
//    {0d, 0d, 0d, 1d}
//    
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d},
    {1/4d, 1/4d, 1/4d, 1/4d}
    
  };
  
  
  public Action policy(int state) {
    int max = Integer.MIN_VALUE;
    int maxAt = 0;
    for (int i = 0; i < Action.values().length; i++) {
      if (pi[state][i] > max) {
        maxAt = i;
      }
    }
    return Action.values()[maxAt];
  }
  
  private int rewardFunction(int state, Action action, int statedash) {
    return -1;
  }
  
  public double[] stateValues = new double[16]; 
  
  public double[] stateValuesDash = new double[16];
  
  public void evaluate() {
    double delta;
    int iterationNumber = 0;
    do {
      System.out.println(++iterationNumber);
      stateValues = Arrays.copyOf(stateValuesDash, stateValues.length);
      delta = 0;
      for (int state : Arrays.copyOfRange(states, 1, 15)) {
        double temp = stateValues[state];
        double stateValue = 0;
        for (int statedash: states) {
          stateValue += p(statedash, state, policy(state))*(rewardFunction(state, policy(state), statedash) + GAMMA*stateValues[statedash]);
        }
        stateValuesDash[state] = stateValue;
        //update statevalue immidiately. 
        //No need to wait till all states are done. 
        stateValues[state] = stateValuesDash[state];//comment if you don't want this
        delta = Math.max(delta, Math.abs(temp - stateValuesDash[state]));
      }
    } while (delta > THETA);
    System.out.println("Converged after " + iterationNumber + " iteration for THETA " + THETA);
    System.out.println("delta: "+ delta);
  }
  
  public void improve(){
    
    do {
      boolean policyStable = true;
      for (int state : Arrays.copyOfRange(states, 1, 15)) {
        Action temp = policy(state);
        double[] tempActions = new double[Action.values().length];
        for (Action action: Action.values()) {
          for (int statedash : Arrays.copyOfRange(states, 1, 15)) {
            tempActions[action.ordinal()] = p(statedash, state, action) * (rewardFunction(state, action, statedash) + GAMMA*stateValues[statedash]);
          }
          pi[state] = tempActions;
        }
        if (!policy(state).equals(temp)) {
          policyStable = false;
        }
      }
      if (policyStable) {
        //print policy
        System.out.println(stateValues);
        System.out.println(pi);
        break;
      } else {
        evaluate();
      }
    } while(true);
      
  }
  
  public static void main(String[] argv) {
    PolicyIterationMethod policyIterationMethod = new PolicyIterationMethod();
    policyIterationMethod.evaluate();
    policyIterationMethod.improve();
  }
  
}
