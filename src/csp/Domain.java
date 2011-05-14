package csp;

import java.util.Arrays;
import java.util.HashSet;

import csp.MapColoring.Value;

@SuppressWarnings("serial") class Domain extends HashSet<Value> {
    static Domain fullDomain() {
        Domain d = new Domain();
        d.addAll(Arrays.asList(Value.values()));
        return d;
    }

}