# Zero-Friction Accountability via Native Sharing Intents

We decided to implement buddy updates using standard Android sharing intents (Intent.ACTION_SEND / Intent.ACTION_SENDTO). This avoids requiring sensitive SEND_SMS permissions on Google Play, costs nothing in server overhead, and lets users share via any messaging platform (SMS, WhatsApp, Signal, Telegram) without requiring buddy app downloads.
