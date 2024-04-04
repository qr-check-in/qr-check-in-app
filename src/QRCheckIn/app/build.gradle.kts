plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.qrcheckin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.qrcheckin"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.activity:activity:1.8.2")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-firestore:24.11.0")
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    implementation("com.firebaseui:firebase-ui-firestore:8.0.2")
    implementation("androidx.camera:camera-core:1.3.2")
    implementation("androidx.camera:camera-camera2:1.3.2")
    implementation("androidx.camera:camera-lifecycle:1.3.2")
    implementation("androidx.camera:camera-view:1.3.2")
    implementation ("com.google.api:api-common:2.2.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("org.mockito:mockito-core:5.10.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    testImplementation ("org.mockito:mockito-core:5.10.0")
    testImplementation ("com.google.firebase:firebase-firestore:24.11.0")


    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation("com.google.firebase:firebase-database:20.3.1")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")

    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation ("org.mockito:mockito-android:4.0.0") //
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test:rules:1.5.0")

    // If you need additional functionality, consider adding extensions
    // implementation "androidx.camera:camera-extensions:1.0.0-alpha24"
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")


    // QRCode Scanner
    implementation ("com.google.android.gms:play-services-auth:21.0.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.mlkit:barcode-scanning:17.2.0")

    // Bottom Sheet
    implementation ("com.google.android.material:material:1.11.0")

    // Expresso Intent
    implementation ("androidx.test.espresso:espresso-intents:3.5.1")

    implementation ("androidx.test:runner:1.5.2")
    implementation ("androidx.test:rules:1.5.0")
    implementation ("androidx.test.espresso:espresso-core:3.5.1")

    // Glide
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

}