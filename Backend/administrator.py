import webapp2

""" Post API """
class GameAdministrator(webapp2.RequestHandler):
    def get(self, administrator_name, administrator_email,administrator_password):
        self.response.write('The administrator_name %s administrator_email is %s administrator_password is %s' %(administrator_name, administrator_email, administrator_password) )