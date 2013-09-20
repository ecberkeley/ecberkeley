package org.ecberkeley.css.generics;

import java.lang.reflect.Constructor;

public class GetPut {

    public static class RuleResult<T> {
        T result;

        public RuleResult(T t) {
            result = t;
        }

        public T getResult() {
            return result;
        }
    }

    public static class Risk<T> extends RuleResult {
        public Risk(T t) {
            super(t);
        }
    }

    public static class Regulation<T> extends RuleResult {
        public Regulation(T t) {
            super(t);
        }
    }

    public enum RiskFactor {RISKS, REGULATIONS}

    public static <T> RuleResult<T> engineSingle(RiskFactor factor) throws Exception {
        Risk<String> risk = new Risk<String>("Timespan too long");
        Regulation<String> regulation = new Regulation<String>("USPA#1009");
        switch (factor) {
            case RISKS:
                return risk;
            case REGULATIONS:
                return regulation;
            default:
                return new Risk<String>("Unknown Risk");
        }
    }

    public static <T> RuleResult<T>[] engine(Class<T> ct) throws Exception {
        RuleResult<T>[] arr = (RuleResult<T>[]) java.lang.reflect.Array.newInstance(ct, 10);
        Risk<String> risk = new Risk<String>("Timespan too Short");
        Regulation<String> regulation = new Regulation<String>("USPA#4011");
        arr[0] = risk;
        arr[1] = regulation;
        return arr;
    }

    public static void main(String[] args) throws Exception {
        RuleResult<String> res = GetPut.engineSingle(RiskFactor.REGULATIONS);
        System.out.println("res: " + res.getResult());

        res = GetPut.engineSingle(RiskFactor.RISKS);
        System.out.println("res: " + res.getResult());

        System.out.println("res: " + GetPut.engine(String.class));
    }

}
