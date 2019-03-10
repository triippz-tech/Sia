/*
 *    Copyright 2019 Mark Tripoli
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.trievosoftware.discord.utils;

public class Conversions {

    public static final String DEGREE = "\u00b0";

    public static String convertTemp(Double kelvin, String temp)
    {
        switch (temp.toUpperCase())
        {
            case "C":
                return kToC(kelvin);
            case "F":
                return kToF(kelvin);
            case "CELSIUS":
                return kToC(kelvin);
            case "FAHRENHIET":
                return kToF(kelvin);
            default:
                return kToF(kelvin);
        }
    }

    public static String kToF(Double kelvin)
    {
        Double celsius = (kelvin - 273);
        double fahrenhiet =  (celsius * 9.0/5.0) + 32.0;
        return (int) fahrenhiet + DEGREE + "F";
    }

    public static String kToC(Double kelvin)
    {
        return (int) (kelvin - 273) + DEGREE + "C";
    }
}
