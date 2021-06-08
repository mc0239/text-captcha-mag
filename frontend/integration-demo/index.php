<html lang="en">
<head>
    <title>Text CAPTCHA Integration Demo</title>
    <link rel="stylesheet" href="style.css" />
    <style>

        form > * {
            display: block;
            margin: 6px;
        }
        form > textarea {
            width: 600px;
            height: 400px;
        }
        form > button[type=submit] {
            padding: 8px;
        }
    </style>
</head>
<body>
    <h1>Text CAPTCHA Integration Demo</h1>
    <form method="post" action="submit.php">
        <label for="content">Text to create CAPTCHA for (i.e. copy text from an article):</label>
        <textarea id="content" name="content"></textarea>
        <button type="submit">Submit</button>
    </form>
</body>
</html>