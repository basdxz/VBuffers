package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.accessor.front.GetterFront;
import com.github.basdxz.vbuffers.accessor.front.SetterFront;

public interface FrontendAccessorExample {
    @SetterFront.Access
    void set0(int input);

    @SetterFront.Access
    void set1(int input, int offsetStrides);

    @SetterFront.Access
    FrontendAccessorExample set2(int input);

    @SetterFront.Access
    FrontendAccessorExample set3(int input, int offsetStrides);

    @SetterFront.Access
    int set4(int input);

    @SetterFront.Access
    int set5(int input, int offsetStrides);

    @GetterFront.Access
    void get0(int output);

    @GetterFront.Access
    void get1(int output, int offsetStrides);

    @GetterFront.Access
    FrontendAccessorExample get2(int output);

    @GetterFront.Access
    FrontendAccessorExample get3(int output, int offsetStrides);

    @GetterFront.Access
    int get4();

    @GetterFront.Access
    int get5(int offsetStrides);

    @GetterFront.Access
    int get6(int output);

    @GetterFront.Access
    int get7(int output, int offsetStrides);
}
