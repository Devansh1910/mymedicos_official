plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.medical.my_medicos"
    compileSdk = 34

    buildFeatures {
        viewBinding
    }

    defaultConfig {
        applicationId = "com.medical.my_medicos"
        minSdk = 24
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 34
        versionCode = 5
        versionName = "1.0.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures{

        viewBinding=true
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        viewBinding = true
    }
}



dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-analytics")
//    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment:2.7.5")
    implementation("androidx.navigation:navigation-ui:2.7.5")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.firebase:firebase-sessions:1.2.0")
    implementation("androidx.media3:media3-common:1.2.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("com.google.firebase:firebase-messaging:23.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.hbb20:ccp:2.7.3")
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    //....Image Zoom
    implementation ("com.github.chrisbanes:PhotoView:2.3.0")
    ///........
    implementation("com.intuit.ssp:ssp-android:1.1.0")
//    implementation("com.google.firebase:firebase-database")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.22")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.firebaseui:firebase-ui-database:8.0.2")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
    // Additional dependencies from the first code block
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-firestore:24.9.1")
    implementation ("com.squareup.picasso:picasso:2.71828")
    //....
    implementation ("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")
    /* For Slider - End */

    /* For Rounded Image View */
    implementation ("com.makeramen:roundedimageview:2.3.0")
    /* For Material Search Bar */
    implementation ("com.github.mancj:MaterialSearchBar:0.8.5")
    implementation ("me.relex:circleindicator:2.1.6")
    implementation ("org.imaginativeworld.whynotimagecarousel:whynotimagecarousel:2.1.0")
    /* For Slider - End */
    implementation ("com.android.volley:volley:1.2.1")
    //..
    implementation ("com.airbnb.android:lottie:6.2.0")
    //..
    implementation ("com.github.hishd:TinyCart:1.0.1")
    implementation ("com.github.delight-im:Android-AdvancedWebView:v3.2.1")
    //...Bottom navigation bar
    implementation("com.exyte:animated-navigation-bar:1.0.0")
    //...Shimmer....
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    //.....
    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.19.1")

}