import argparse
import io
import json
import re

try:
  to_unicode = unicode
except NameError:
  to_unicode = str


class UnsupportedDataTypeError(LookupError):
  '''raise this when the data type is not supported'''


def getNominalArr(dataTypeOrValues):
  nominalArr = []

  for temp in dataTypeOrValues.split(','):
    nominalArr.append(temp)

  return nominalArr


def convert_arff_to_json(dir, arff_file):
  # Reading the csv file from the directory
  arff_file_path = dir + "/" + arff_file
  json_file_path = dir + "/" + arff_file[:-4] + "json"

  print('Reading the Arff file {}, writing the json {}'.format(arff_file_path,
                                                               json_file_path))

  with open(arff_file_path) as f:
    content = f.readlines()
    print content
    content = [x.strip() for x in content]

  data = {}
  features = []
  data['features'] = features

  for row in content:
    relation_regex = r"@RELATION\s+(\S+)"
    att_regex = r"@ATTRIBUTE\s+(\S+)\s+(\S+)"
    nominal_regex = r"{(\S+)}"

    if re.search(relation_regex, row, re.IGNORECASE):
      # print(row)
      match = re.search(relation_regex, row, re.IGNORECASE)
      data['name'] = match.group(1)
      data['shortName'] = match.group(1)
    elif re.search(att_regex, row, re.IGNORECASE):
      # print(row)
      match = re.search(att_regex, row, re.IGNORECASE)
      feature = {'name': match.group(1)}
      features.append(feature)

      dataTypeOrValues = match.group(2)

      if re.search(nominal_regex, dataTypeOrValues, re.IGNORECASE):
        nominalMatch = re.search(nominal_regex, dataTypeOrValues, re.IGNORECASE)
        feature['dataType'] = "nominal"
        feature['values'] = getNominalArr(nominalMatch.group(1))
      else:
        dataType = dataTypeOrValues.lower()

        if dataType == "real" or dataType == "integer":
          dataType = "numeric"
        if dataType == "date" or dataType == "relational":
          raise UnsupportedDataTypeError(dataType)

        feature['dataType'] = dataType

      if feature['name'] == "class":
        feature['class'] = True

  with io.open(json_file_path, 'w', encoding='utf8') as outfile:
    str_ = json.dumps(data, indent=2, sort_keys=False, separators=(',', ':'),
                      ensure_ascii=False)
    outfile.write(to_unicode(str_))
    print str_


if __name__ == '__main__':
  parser = argparse.ArgumentParser(
      description="This script converts an arff file to corresponding json file for the models")

  parser.add_argument('-d', '--directory', help='Directory of the arff file',
                      required=True)
  parser.add_argument('-af', '--arff_file', help='Arff file', required=True)

  args = parser.parse_args()

  print("Directory : %s" % args.directory)
  print("Arff file : %s" % args.arff_file)

  convert_arff_to_json(args.directory, args.arff_file)
