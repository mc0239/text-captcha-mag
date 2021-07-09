from fastapi import Body, FastAPI
import classla

classla_processors = 'tokenize,pos,lemma'

classla.download('sl', processors=classla_processors)
nlp = classla.Pipeline('sl', processors=classla_processors)


app = FastAPI(
    title='CLASSLA REST API',
    description='CLASSLA (https://github.com/clarinsi/classla) text processing through a REST API',
)


@app.post("/process")
async def process(
        body: str = Body(
            example='France Prešeren je rojen v Vrbi.',
            default=None,
            media_type='text/plain'
        )
):
    return nlp(body).to_dict()
