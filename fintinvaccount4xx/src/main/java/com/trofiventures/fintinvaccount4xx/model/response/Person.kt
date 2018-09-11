package com.trofiventures.fintinvaccount4xx.model.response

import java.io.Serializable
import java.util.*

class Person(var id: String,
             var tenant_name: String,
             var person_type: String,
             var firstname: String,
             var lastname: String,
             var language: String,
             var language_country: String,
             var timezone: String,
             var status: String,
             var status_change_datetime: Date,
             var activation_datetime: Date,
             var created_date: Date) : Serializable