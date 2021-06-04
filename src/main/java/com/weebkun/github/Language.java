/*
Copyright 2020 weebkun

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.weebkun.github;

import com.google.gson.annotations.SerializedName;

/**
 * lists the number of bytes written for each language.
 */
public class Language {
    public int C;

    @SerializedName("C++")
    public int Cpp;

    @SerializedName("C#")
    public int CSharp;

    @SerializedName("Visual Basic")
    public int VisualBasic;

    @SerializedName("Objective-C")
    public int ObjectiveC;
    public int Python;
    public int Java;
    public int HTML;
    public int Perl;
    public int Groovy;
    public int Shell;
    public int ASP;
    public int JavaScript;
    public int PHP;
    public int TypeScript;
    public int Go;
    public int Ruby;
    public int Scala;
    public int Rust;
    public int Lua;
    public int CoffeeScript;
    public int Haskell;
    public int Kotlin;
}
