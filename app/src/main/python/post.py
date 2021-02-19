import tweepy
import pandas as pd
import random
import time
from os.path import dirname, join


def publicar(tweet):

    consumer_key='Your Key here'
    consumer_secret_key='Your Secret Key here'
    access_token='Your Access Token here'
    access_token_secret='Your Access Token Secret here'


    auth=tweepy.OAuthHandler(consumer_key,consumer_secret_key)

    auth.set_access_token(access_token,access_token_secret)

    api=tweepy.API(auth)
    print("DEBUG -- Autenticado!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    

    api.update_status(tweet)
    return





