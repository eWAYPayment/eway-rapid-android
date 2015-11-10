# eWAY Rapid Android Library

Quickly integrate eWAY payments into your Android app with this SDK! This SDK provides the following functions:

 - Encrypt card details for passing through the merchant's servers to eWAY
 - Complete a payment on a user's Android device
 - Translate response messages to user friendly text

Check out the [eWAY Android SDK Getting Started Guide](https://www.eway.com.au/developers/sdk/android) and the example projects for details on how to integrate eWAY with your app.

Sign up with eWAY at:
 - Australia:    https://www.eway.com.au/
 - New Zealand:  https://eway.io/nz/
 - UK:           https://eway.io/uk/
 - Hong Kong:    https://eway.io/hk/
 - Malaysia:     https://eway.io/my/
 - Singapore:    https://eway.io/sg/

For testing, get a free eWAY Partner account: https://www.eway.com.au/developers

## Requirements

The eWAY Android SDK requires a minimum Android SDK version 17

This SDK also supports [ReactiveX](http://reactivex.io/), which requires [rxandroid](https://github.com/ReactiveX/RxAndroid) or [rxjava](https://github.com/ReactiveX/RxJava)

## Installation

Installing eWAY Android SDK is quick and easy:

### Gradle

Add the maven repository to your project's build gradle file under all projects:

```
repositories {
    jcenter()
    maven { url "http://dl.bintray.com/webactive/maven" }
}
```

Then add the dependency to the dependencies section of app’s `build.gradle`:

```
compile "com.eway.payment:android-sdk:1.+"
```

## Usage

For the a complete guide to using the eWAY Android SDK, check out the [Getting Started]((https://www.eway.com.au/developers/sdk/android)) guide along with the example projects.

## Example Projects

Example projects are included in the eWAY Android SDK GitHub repository to demonstrate the available functions. There are three projects to demonstrate different scenario of the SDK; synchronous, asynchronous and rxjava. To view and use an example;

1. Clone the repository to your development machine

```
git clone https://github.com/eWAYPayment/eway-rapid-android.git
```

2. Open the project in Android Studio and let Gradle sync. 
3. Select a sample in Android Studio - optional: update the `PublicAPIKey` in the SampleMainActivity file to the one in your Sandbox.
4. Hit the run button to view the sample in action.


## License

The MIT License (MIT). Please see [License File](LICENSE.md) for more information.
