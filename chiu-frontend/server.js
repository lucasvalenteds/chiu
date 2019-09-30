const express = require("express");
const path = require("path");

const port = process.env.PORT || 8081;

express()
    .use(express.static(path.join(__dirname + "/dist")))
    .get("/", (req, res) => res.render(path.join(__dirname + "/dist/index.html")))
    .listen(port, () => console.log("Server running on port", port))
