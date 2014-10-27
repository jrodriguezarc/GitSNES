import webapp2
import datetime
from model import GameDB, UserDB, AdministratorDB, FavoriteDB, LikeDB, PendingGameDB
from webapp2_extras import json
from google.appengine.ext import db
#_______________________________________________________________________________________________________________________________________________
""" GET API """

class Games(webapp2.RequestHandler):
    def get(self):
        self.response.content_type = 'application/json' 
        games = GameDB.all()
        games.order("-uploaddate")
        result=[]
        for game in games:
            result.append({'id': str(game.key()), 
                            'name': game.name,
                            'uploader': game.uploader,
                            'description': game.description,
                            'category': game.category,
                            'file_url': game.file_url,
                            'image_url': game.image_url,
                            'uploaddate': str(game.uploaddate),})
        self.response.write(json.encode(result))
#_______________________________________________________________________________________________________________________________________________
class GamesSearch(webapp2.RequestHandler):
    def get(self,game_key):
        self.response.content_type = 'application/json' 
        game = GameDB.get(game_key)
        obj= {'id': 'cambiar', 
                            'name': game.name,
                            'uploader': game.uploader,
                            'description': game.description,
                            'category': game.category,
                            'file_url': game.file_url,
                            'image_url': game.image_url,
                            'uploaddate': str(game.uploaddate),}
        self.response.write(json.encode(obj))
#_______________________________________________________________________________________________________________________________________________
class GamesRecomendation(webapp2.RequestHandler):
    def get(self, game_id, user_id):
        self.response.content_type = 'application/json' 
        obj = {
            'id': 'some var', 
            'name': 'some var',
            'uploader': 'some var',
            'descripction': 'some var',
            'category': 'some var',
            'uploaddate': 'some var',
          } 
        self.response.write(json.encode(obj))
#_______________________________________________________________________________________________________________________________________________
class GameProfile(webapp2.RequestHandler):
    def get(self, game_id):
        self.response.write('The game_is %s' %game_id )
#_______________________________________________________________________________________________________________________________________________
class GamesFavorite(webapp2.RequestHandler):
    def get(self, user_id):
        self.response.write('The game_is %s' %user_id )
#_______________________________________________________________________________________________________________________________________________

""" POST API """
class GameLike(webapp2.RequestHandler):
    def post(self, game_id, user_id):
        self.response.write('The game_is %s and user_id is %s' %(game_id, user_id) )
#_______________________________________________________________________________________________________________________________________________
class GameFavorite(webapp2.RequestHandler):
    def post(self, game_id, user_id):
        self.response.write('The game_is %s and user_id is %s' %(game_id, user_id) )
#_______________________________________________________________________________________________________________________________________________
class Game(webapp2.RequestHandler):
    def post(self, uploader, game_name, game_description, game_category,_image,_file):
        game=GameDB(name=game_name,
            uploader=uploader,
            description=game_description,
            file_url=_file,
            image_url=_image,
            category=game_category)
        game.uploaddate = datetime.datetime.now().date()
        game.put()
        game_id = str(game.key().id())

        gamepending= PendingGameDB(game_id=game_id)
        gamepending.put()       
#_______________________________________________________________________________________________________________________________________________
#name = self.request.get("name")