import yaml
from argparse import ArgumentParser, Namespace

from flask import Flask, jsonify, request
from icecream import ic, install
install()

from model import Model
from deblur import get_deblur_model, deblur_image
from utils import get_image_from_url, sort_text, BadRequest


app = Flask(__name__)
@app.route('/detect')
def detect():
    try:
        global model, opt
        url = request.args.get('url')
        bbox = request.args.get('bbox', default=False, type=bool)
        deblur = request.args.get('deblur', default=False, type=bool)
        
        opt.img = get_image_from_url(url)
        if deblur:
            opt.img = deblur_image(deblur_model, opt.img)
        items = model.readtext(**vars(opt))[0]['result']
        if bbox:
            return jsonify(items)
        return sort_text(items)
    except BadRequest:
        return 'Bad request', 400

if __name__ == '__main__':
    parser = ArgumentParser()
    parser.add_argument('--option', '-o', type=str, default='options/default.yml',
        help='Option file')
    args = parser.parse_args()

    opt = Namespace(**yaml.safe_load(open(args.option, 'r')))
    opt.details = True
    opt.imshow = False
    opt.print_result = False
    model = Model(
        det=opt.model_det,
        recog=opt.model_recog,
        config_dir='configs/',
    )

    deblur_model = get_deblur_model()

    app.run()