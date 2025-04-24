package com.example.bryan_34309861_a3_app.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bryan_34309861_a3_app.R

@Composable
fun WelcomeScreen(paddingValues: PaddingValues) {
    Column(modifier = Modifier.padding(paddingValues).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(100.dp))
        Text("NutriTrack",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold)
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp)
        )
        Text("This app provides general health and nutrition information" +
                "\nfor educational purposes only, it is not intended as" +
                "\nmedical advice, diagnosis, or treatment. Always consult a" +
                "\nqualified healthcare professional before making any " +
                "\nchanges to your diet, exercise, or health regimen." +
                "\nUse this app at your own risk." +
                "\nIf you'd like to an Accredited Practicing Dietitian (APD)," +
                "\nplease visit the Monash Nutrition/Dietetics Clinic" +
                "\n(discounted rates for students):" +
                "\nhttps://ww.monash.edu/medicine/scs/nutrition/clinics/nutrition"
            , textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(48.dp))
        val context = LocalContext.current
        Button(onClick = {
            /*TODO*/

        },
            modifier = Modifier.size(width = 300.dp, height = 50.dp) ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(48.dp))
        Text("Bryan Lau (34309861)",
            textAlign = TextAlign.Center,
            fontSize = 12.sp)
    }
}