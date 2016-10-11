# Toradocu: automated generation of test oracles from Javadoc documentation

[![Build Status](https://travis-ci.org/albertogoffi/toradocu.svg?branch=master)](https://travis-ci.org/albertogoffi/toradocu)

Toradocu generates test oracles from the Javadoc documentation of a
class. Toradocu is described in the paper
[*Automatic Generation of Oracles for Exceptional Behaviors*](http://star.inf.usi.ch/star/papers/16-issta-toradocu.pdf)
by Alberto Goffi, Alessandra Gorla, Michael D. Ernst, and Mauro Pezzè (presented
at [ISSTA 2016](https://issta2016.cispa.saarland)).

Toradocu takes the source code of a class as input and produces a set of
[aspects](https://eclipse.org/aspectj/).


## Building Toradocu
To compile Toradocu run the command: `./gradlew shadowJar`
This will create the file `build/libs/toradocu-1.0-all.jar`.
Notice that building Toradocu requires Java JDK 1.8+.


## Running Toradocu
Toradocu is a command-line tool.

To get the list of all command-line parameters, execute

    java -jar toradocu-1.0-all.jar --help

A typical Toradocu invocation looks like this:

    java -jar toradocu-1.0-all.jar \
       --target-class mypackage.MyClass \
	   --test-class mypackage.MyTest \
	   --source-dir project/src \
	   --class-dir project/bin \
       --aspects-output-dir aspects

Internally Toradocu executes the `javadoc` tool. Every option starting with `-J`
will be passed to the `javadoc` tool. For example, you can specify the directory
where to save the Javadoc documentation with `-J-d=javadoc_output`. You can
customize the behavior of the `javadoc` tool using all its options.


## Using Toradocu Aspects
With those options, Toradocu generates
Toradocu generates [AspectJ aspects](https://eclipse.org/aspectj/) in the
directory specified with the option `--aspects-output-dir`. In the aspects output directory,
Toradocu places the source code of the aspects and an `aop.xml` that lists the generated aspects
and that is used by the AspectJ compiler.

Aspects generated by Toradocu are standard AspectJ aspects and can be used to instrument an
existing test suite. This is done by using the AspectJ compiler to weave the source files
under test with the aspects generated by Toradocu. For an example, see the files in the `example`
directory.


To augment an existing test suite with Toradocu's oracles you have to:

1. Generate the aspects with Toradocu.
2. Compile the generated aspects.
3. Weave the existing test suites and the system under test.
4. Run the weaved test suite.
Please refer to the [AspectJ documentation](https://eclipse.org/aspectj/doc/released/devguide/ajc-ref.html)
for more information.

To compile the aspects you can use `javac`. Just be sure that
[JUnit](http://junit.org/junit4/), the AspectJ weaver, and your system under
test are on the classpath.

To weave the existing test suite and the system under test, you can use the
[AspectJ compiler](https://eclipse.org/aspectj/doc/next/devguide/ajc-ref.html).
The weaving of the system under test is needed for aspects that check the
behavior of constructors.

Run weaved test suite as a normal test suite, just be sure to have AspectJ on
the classpath.


## Notes for the contributors
The source code of this project follows the
[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). The
style of the code is automatically checked by a git pre-commit hook. Also, the
hook prevents any commits of files containing trailing spaces. The hook is
automatically installed as soon as any gradle task is executed.

If a commit fails for the improper format of some file, you can format your code
according the style guide using the following command:

	python run-google-java-format.py [file1 file2 ...]

Other information for the contributors can be found in the
[wiki pages](https://github.com/albertogoffi/toradocu/wiki).
