package com.example.actividad2

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://hcqgjqcxfzkjxybvztix.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImhjcWdqcWN4Znpranh5YnZ6dGl4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzU2MDE4NDMsImV4cCI6MjA5MTE3Nzg0M30.YmPyBuMieTfFdgl--tLnRm-H7YsoWaEL2JE1TGocQM0"
    ){
        install(Postgrest)
        install(Auth)

    }
}