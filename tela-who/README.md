# Tela Who?

Tela Who? is a web application for companies, brands, and regular users, that allows them to obtain a better understanding about how a user is connected with them, so that they can improve their relationship and target similar users. 

Developed by [Álvaro Reneses](http://www.reneses.io) as his Final Degree Project (Software Engineering) at [University of Oviedo](http://www.uniovi.es).

## Deployed version

This application is deployed at: [https://tela-who.herokuapp.com](https://tela-who.herokuapp.com).

However, as commented in the report, only sandbox users can access due to the restrictions of the [Instagram Sandbox Mode](https://www.instagram.com/developer/sandbox/). The application is currently waiting for Instagram approval; until then, you can be added as a sandbox user by contacting me at me@reneses.io.

## Building

**Tela Who?** building requires **npm** and **Webpack**.

In order to generate the production files, execute:

```bash
npm install
```

The command will automatically execute Webpack, and the final files will be placed at the `dist/` folder.

## Running the Built-In Server

Tela Who? is shipped with a Node server, so that you can start the application right after building it.

In order to do it, just execute:

``` bash
npm install  # If not executed before
npm start
```

## Deploying Tela Who? in a Server

You can deploy Tela Who? in any type of server, as long as it redirects all the not found requests to the `index.html` file. 

In the built-in Node server, this is done by including the following lines:

```javascript
var publicPath = path.resolve(__dirname, 'dist');
app.use(express.static(publicPath));
app.all('*', function (req, res) {
    res.status(200).sendFile(path.join(publicPath, 'index.html'));
});
```
