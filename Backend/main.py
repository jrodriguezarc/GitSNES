import webapp2


from game import Games, GamesRecomendation, GameProfile, GamesFavorite, GameLike, GameFavorite, Game, GamesSearch
from administrator import GameAdministrator


app = webapp2.WSGIApplication([
				    (r'/games', Games),
				    (r'/gamesrecomendation/(\w+)/(\w+)', GamesRecomendation),
				    (r'/gameprofile/(\w+)', GameProfile),
				    (r'/gamesfavorite/(\w+)', GamesFavorite), 
				    (r'/gamelike/(\w+)/(\w+)', GameLike),
				    (r'/gamefavorite/(\w+)/(\w+)', GameFavorite),
				    (r'/game/(\w+)/(\w+)/(\w+)/(\w+)/(\w+)/(\w+)', Game),
				    (r'/gamesearch/([a-zA-Z0-9_-]+)', GamesSearch),
				    (r'/administrator/(\w+)/(\w+)/(\w+)', GameAdministrator)], debug=True)