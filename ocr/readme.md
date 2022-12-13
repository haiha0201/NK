# OCR API

## Run

Create python env:

` conda env create -f environment.yml`

`conda activate mmocr`

Start service:

`python -W ignore api.py -o options/v1.yml`

The service will run on http://127.0.0.1:5000 by default

## API

### `/detect`

Params:

* `url`: url of image in string

Response: text

Example:

* Query:

```
http://127.0.0.1:5000/detect?url=https://www.wikihow.com/images/thumb/c/c2/Analyze-a-Scene-in-a-Film-Step-16-Version-2.jpg/aid1610856-v4-1200px-Analyze-a-Scene-in-a-Film-Step-16-Version-2.jpg
```

* Response:

```
conclude by restating thesis
in the end even the characters of blue
ruin know how pointless their feud is
but revenge much like every taut minute
this thriller far too addictive to
give until the bitter end
wikihow to analyze scene in film
```



