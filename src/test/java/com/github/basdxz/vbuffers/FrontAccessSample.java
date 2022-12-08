package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.access.front.GetterFront;
import com.github.basdxz.vbuffers.access.front.SetterFront;

public interface FrontAccessSample {
    @SetterFront.Access
    void set0(int input);

    @SetterFront.Access
    void set1(int input, int offsetStrides);

    @SetterFront.Access
    FrontAccessSample set2(int input);

    @SetterFront.Access
    FrontAccessSample set3(int input, int offsetStrides);

    @SetterFront.Access
    int set4(int input);

    @SetterFront.Access
    int set5(int input, int offsetStrides);

    @GetterFront.Access
    void get0(int output);

    @GetterFront.Access
    void get1(int output, int offsetStrides);

    @GetterFront.Access
    FrontAccessSample get2(int output);

    @GetterFront.Access
    FrontAccessSample get3(int output, int offsetStrides);

    @GetterFront.Access
    int get4();

    @GetterFront.Access
    int get5(int offsetStrides);

    @GetterFront.Access
    int get6(int output);

    @GetterFront.Access
    int get7(int output, int offsetStrides);
}
