<html lang="en">
<head>
    <title>Text CAPTCHA Integration Demo</title>
    <link rel="stylesheet" href="style.css" />
    <style>
        article {
            margin-top: 24px;
            margin-bottom: 24px;
            text-align: justify;
        }
    </style>
</head>
<body>
    <h1>Text CAPTCHA Integration Demo</h1>

    You've submitted:
    <article>
    <?= htmlspecialchars($_POST["content"]) ?>
    </article>

    <div id="text-captcha"></div>

    <a href="index.php">Go back.</a>

    <script>

        function extractArticleText() {
            return document.querySelector("article").textContent;
        }

    </script>
    <script src="captcha.js"></script>
    <script>



        console.log(TextCaptcha);

    </script>

</body>
</html>