plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKapt)
}

android {
    namespace = "com.palgao.menu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.palgao.menu"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
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
    packagingOptions {
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.recyclerview)
    implementation(libs.room.common)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Dependencia para Okio (generalmente es transitiva con OkHttp, pero asegúrate de incluirla si es necesario)
    // implementation(libs.okio)
    implementation("com.squareup.okio:okio:3.5.0") // Verifica si necesitas ambas

    implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")

    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // Dependencias para OkHttp
    implementation(libs.okhttp)

    // Librerías de networking y servicios web:
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.library)
    implementation(libs.google.http.client)
    implementation(libs.http.client.google.http.client.android)
    implementation(libs.http.client.google.http.client.jackson2)
    implementation(libs.jackson.core)
    implementation(libs.slf4j.log4j12)
    implementation(libs.unirest.java)
    implementation(libs.gson)
    implementation(libs.listenablefuture)
    implementation(libs.guava)
//    implementation(fileTree(dir: 'libs', include: ['*.jar']))
//    implementation(libs.socket.io.client) {
//        exclude group: 'org.json', module: 'json'
//    }

    // Librerías de gestión de permisos:
    implementation(libs.permissionsdispatcher)
    annotationProcessor(libs.permissionsdispatcher.processor)

    // Mapa
    implementation("org.osmdroid:osmdroid-android:6.1.14")

    //hdodenhof para imagen circular
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("com.squareup.picasso:picasso:2.71828")

    implementation ("io.socket:socket.io-client:2.1.0")

}
