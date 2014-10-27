from google.appengine.ext import db

class GameDB(db.Model):
    name = db.StringProperty(required=True)
    uploader = db.StringProperty(required=True)
    description = db.StringProperty(required=True)
    category = db.StringProperty(required=True)
    image_url = db.StringProperty()
    file_url= db.StringProperty()
    uploaddate = db.DateProperty()

class UserDB(db.Model):
    name = db.StringProperty(required=True)
    email = db.StringProperty(required=True)

class AdministratorDB(db.Model):
    name = db.StringProperty(required=True)
    email = db.StringProperty(required=True)
    password = db.StringProperty(required=True)    

class FavoriteDB(db.Model):
    game_id = db.StringProperty(required=True)
    user_id = db.StringProperty(required=True)

class LikeDB(db.Model):
    game_id = db.StringProperty(required=True)
    user_id = db.StringProperty(required=True)

class PendingGameDB(db.Model):
    game_id = db.StringProperty(required=True)

