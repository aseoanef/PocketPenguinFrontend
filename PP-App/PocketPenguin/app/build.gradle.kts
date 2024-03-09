plugins {
    id("com.android.application")
}
val graphviewVersion by extra("4.2.2")

android {
    namespace = "com.example.pocketpenguin"

    compileSdkVersion(rootProject.extra["compileSdkVersion"] as Int)
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pocketpenguin"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation ("com.squareup.picasso:picasso:2.71828")

    implementation ("com.google.android.material:material:1.4.0")

    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.jjoe64:graphview:4.2.2")
    implementation ("com.android.volley:volley:1.2.1")

    implementation("androidx.navigation:navigation-fragment:2.7.5")
    implementation("androidx.navigation:navigation-ui:2.7.5")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}