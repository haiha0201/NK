import yaml
from argparse import ArgumentParser, Namespace

from flask import Flask, jsonify
from icecream import ic, install
install()

from model import Model
from utils import get_image_from_url


app = Flask(__name__)
@app.route('/test')
def test():
    try:
        global model, opt
        opt.img = get_image_from_url('https://www.robots.ox.ac.uk/~vgg/software/textspot/text.png')
        # opt.imshow = True
        # opt.print_result = True
        res = model.readtext(**vars(opt))
        words = res[0]['text']
        print('Detected words:', end=' ')
        print(*words, sep=', ')
        return jsonify({'words': words})

    except Exception as e:
        return jsonify('System Error')


if __name__ == '__main__':
    parser = ArgumentParser()
    parser.add_argument('--option', '-o', type=str, default='options/default.yml',
        help='Option file')
    args = parser.parse_args()
    opt = Namespace(**yaml.safe_load(open(args.option, 'r')))
    model = Model(
        det='PANet_IC15',
        recog='SEG',
        config_dir='configs/',
    )
    app.run()