package com.seemingwang.machineLearning.FlowNode;

import com.seemingwang.machineLearning.Utils.FetchValue;

public class SwitchFlowNode extends FlowNode {
    public Boolean getBinarySwitch() {
        return binarySwitch;
    }

    public void setBinarySwitch(Boolean binarySwitch) {
        this.binarySwitch = binarySwitch;
    }

    @FetchValue
    Boolean binarySwitch;
}
